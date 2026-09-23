//
// Created by neo on 9/22/26.
// Optimized iOS Articles Experience for KMP
//

import SwiftUI
import SharedLogic

// MARK: - Kotlin Flow Extension for Swift
extension Kotlinx_coroutines_coreFlow {
    func stream<T: AnyObject>() -> AsyncStream<T> {
        AsyncStream { continuation in
            let collector = FlowCollector<T> { value in
                continuation.yield(value)
            }
            self.collect(collector: collector) { _ in
                continuation.finish()
            }
        }
    }
}

private class FlowCollector<T: AnyObject>: NSObject, Kotlinx_coroutines_coreFlowCollector {
    private let callback: (T) -> Void

    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }

    func emit(value: Any?, completionHandler: @escaping (Error?) -> Void) {
        if let value = value as? T {
            callback(value)
        }
        completionHandler(nil)
    }
}

// MARK: - ViewModel Wrapper & State Management
@MainActor
final class ArticlesViewModelWrapper: ObservableObject {
    @Published private(set) var state: ArticlesState = ArticlesState.Empty.shared
    @Published var searchText: String = ""

    private let viewModel: ArticlesViewModel
    private var streamTask: Task<Void, Never>?

    init(viewModel: ArticlesViewModel = KoinHelper().articlesViewModel) {
        self.viewModel = viewModel
        self.state = viewModel.articlesState.value as! ArticlesState

        startObserving()
    }

    deinit {
        streamTask?.cancel()
    }

    private func startObserving() {
        streamTask?.cancel()
        streamTask = Task { [weak self] in
            guard let self = self else { return }
            for await currentState in self.viewModel.articlesState.stream() as AsyncStream<ArticlesState> {
                self.state = currentState
            }
        }
    }

    func loadArticles() {
        viewModel.loadArticles()
    }

    var filteredArticles: [Article] {
        guard let successState = state as? ArticlesState.Success else { return [] }
        let articles = successState.items

        let query = searchText.trimmingCharacters(in: .whitespacesAndNewlines).lowercased()
        if query.isEmpty {
            return articles
        } else {
            return articles.filter { article in
                article.title.lowercased().contains(query) ||
                article.description.lowercased().contains(query) ||
                article.content.lowercased().contains(query)
            }
        }
    }
}

// MARK: - ArticlesScreen View
struct ArticlesScreen: View {
    @StateObject private var wrapper = ArticlesViewModelWrapper()
    @State private var shouldOpenAbout = false

    var body: some View {
        NavigationStack {
            ZStack {
                Color(uiColor: .systemGroupedBackground)
                    .ignoresSafeArea()

                switch wrapper.state {
                case is ArticlesState.Loading:
                    ArticleSkeletonListView()

                case is ArticlesState.Success:
                    if wrapper.filteredArticles.isEmpty {
                        ArticleEmptyView(
                            title: wrapper.searchText.isEmpty ? "No Articles Found" : "No Matches Found",
                            message: wrapper.searchText.isEmpty
                                ? "There are no articles available right now. Pull down to refresh."
                                : "No articles matched '\(wrapper.searchText)'. Try another search query.",
                            onRetry: wrapper.searchText.isEmpty ? { wrapper.loadArticles() } : nil
                        )
                    } else {
                        ArticleListView(
                            articles: wrapper.filteredArticles,
                            onRefresh: {
                                wrapper.loadArticles()
                            }
                        )
                    }

                case is ArticlesState.Empty:
                    ArticleEmptyView(onRetry: {
                        wrapper.loadArticles()
                    })

                case let errorState as ArticlesState.Error:
                    ArticleErrorView(
                        errorMessage: errorState.message,
                        onRetry: {
                            wrapper.loadArticles()
                        }
                    )

                default:
                    ArticleSkeletonListView()
                }
            }
            .navigationTitle("Articles")
            .navigationBarTitleDisplayMode(.large)
            .searchable(text: $wrapper.searchText, prompt: "Search articles...")
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        shouldOpenAbout = true
                    } label: {
                        Label("About", systemImage: "info.circle")
                    }
                }
            }
            .popover(isPresented: $shouldOpenAbout) {
                AboutScreen()
            }
        }
    }
}

// MARK: - Article List View
struct ArticleListView: View {
    let articles: [Article]
    let onRefresh: () -> Void

    var body: some View {
        ScrollView {
            LazyVStack(spacing: 16) {
                ForEach(articles, id: \.title) { article in
                    NavigationLink(destination: ArticleDetailView(article: article)) {
                        ArticleRowView(article: article)
                    }
                    .buttonStyle(PlainButtonStyle())
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 12)
        }
        .refreshable {
            onRefresh()
        }
    }
}

// MARK: - Article Row Card View
struct ArticleRowView: View {
    let article: Article

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            if !article.imageUrl.isEmpty, let url = URL(string: article.imageUrl) {
                AsyncImage(url: url) { phase in
                    switch phase {
                    case .success(let image):
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                            .frame(height: 200)
                            .clipped()
                            .cornerRadius(12)
                    case .failure:
                        Image(systemName: "photo")
                            .font(.system(size: 32))
                            .foregroundColor(.secondary)
                            .frame(height: 160)
                            .frame(maxWidth: .infinity)
                            .background(Color(uiColor: .systemGray6))
                            .cornerRadius(12)
                    case .empty:
                        ProgressView()
                            .frame(height: 160)
                            .frame(maxWidth: .infinity)
                            .background(Color(uiColor: .systemGray6))
                            .cornerRadius(12)
                    @unknown default:
                        EmptyView()
                    }
                }
            }

            VStack(alignment: .leading, spacing: 8) {
                Text(article.title)
                    .font(.title3)
                    .fontWeight(.bold)
                    .foregroundColor(.primary)
                    .multilineTextAlignment(.leading)

                if !article.description.isEmpty {
                    Text(article.description)
                        .font(.subheadline)
                        .foregroundColor(.secondary)
                        .lineLimit(3)
                        .multilineTextAlignment(.leading)
                }

                HStack {
                    Label("\(estimatedReadingTime(for: article.content)) min read", systemImage: "clock")
                        .font(.caption)
                        .foregroundColor(.secondary)

                    Spacer()

                    if !article.publishedAt.isEmpty {
                        Text(article.publishedAt)
                            .font(.caption)
                            .foregroundColor(.secondary)
                    }

                    if let url = URL(string: article.imageUrl) {
                        ShareLink(item: url) {
                            Image(systemName: "square.and.arrow.up")
                                .font(.caption)
                                .foregroundColor(.secondary)
                        }
                    }
                }
                .padding(.top, 4)
            }
            .padding(.horizontal, 4)
        }
        .padding(12)
        .background(Color(uiColor: .secondarySystemGroupedBackground))
        .cornerRadius(16)
        .shadow(color: Color.black.opacity(0.05), radius: 6, x: 0, y: 2)
    }
}

// MARK: - Article Detail Screen
struct ArticleDetailView: View {
    let article: Article

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 16) {
                if !article.imageUrl.isEmpty, let url = URL(string: article.imageUrl) {
                    AsyncImage(url: url) { phase in
                        switch phase {
                        case .success(let image):
                            image
                                .resizable()
                                .aspectRatio(contentMode: .fill)
                                .frame(maxHeight: 280)
                                .clipped()
                                .cornerRadius(16)
                        case .failure:
                            Image(systemName: "photo")
                                .font(.system(size: 36))
                                .foregroundColor(.secondary)
                                .frame(height: 180)
                                .frame(maxWidth: .infinity)
                                .background(Color(uiColor: .systemGray6))
                                .cornerRadius(16)
                        case .empty:
                            ProgressView()
                                .frame(height: 180)
                                .frame(maxWidth: .infinity)
                                .background(Color(uiColor: .systemGray6))
                                .cornerRadius(16)
                        @unknown default:
                            EmptyView()
                        }
                    }
                }

                VStack(alignment: .leading, spacing: 12) {
                    Text(article.title)
                        .font(.title.bold())
                        .foregroundColor(.primary)

                    HStack(spacing: 12) {
                        Label("\(estimatedReadingTime(for: article.content)) min read", systemImage: "clock.fill")
                            .font(.caption.bold())
                            .foregroundColor(.secondary)

                        Spacer()

                        if let url = URL(string: article.imageUrl) {
                            ShareLink(item: url) {
                                Image(systemName: "square.and.arrow.up.circle.fill")
                                    .font(.title2)
                                    .foregroundColor(.accentColor)
                            }
                        }
                    }

                    Divider()

                    if !article.description.isEmpty {
                        Text(article.description)
                            .font(.title3)
                            .fontWeight(.medium)
                            .foregroundColor(.secondary)
                            .padding(.vertical, 4)
                    }

                    Text(article.content)
                        .font(.body)
                        .lineSpacing(6)
                        .foregroundColor(.primary)
                }
                .padding(.horizontal, 4)
            }
            .padding(16)
        }
        .navigationBarTitleDisplayMode(.inline)
        .background(Color(uiColor: .systemGroupedBackground))
    }
}

// MARK: - Article Skeleton Loader
struct ArticleSkeletonListView: View {
    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                ForEach(0..<4, id: \.self) { _ in
                    VStack(alignment: .leading, spacing: 12) {
                        ShimmerBox(height: 160)
                            .cornerRadius(12)
                        ShimmerBox(height: 20)
                            .frame(maxWidth: .infinity)
                        ShimmerBox(height: 14)
                            .frame(width: 220)
                    }
                    .padding(12)
                    .background(Color(uiColor: .secondarySystemGroupedBackground))
                    .cornerRadius(16)
                }
            }
            .padding(16)
        }
    }
}

// MARK: - Animated Shimmer Box
struct ShimmerBox: View {
    let height: CGFloat
    @State private var phase: CGFloat = 0

    var body: some View {
        Rectangle()
            .fill(Color(uiColor: .systemGray5))
            .overlay(
                Rectangle()
                    .fill(
                        LinearGradient(
                            gradient: Gradient(colors: [.clear, Color.white.opacity(0.4), .clear]),
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        )
                    )
                    .rotationEffect(.degrees(30))
                    .offset(x: phase)
            )
            .frame(height: height)
            .clipped()
            .onAppear {
                withAnimation(.linear(duration: 1.5).repeatForever(autoreverses: false)) {
                    phase = 300
                }
            }
    }
}

// MARK: - Article Empty View
struct ArticleEmptyView: View {
    let title: String
    let message: String
    let onRetry: (() -> Void)?

    init(
        title: String = "No articles found",
        message: String = "There are no articles available right now. Pull down to refresh or tap retry.",
        onRetry: (() -> Void)? = nil
    ) {
        self.title = title
        self.message = message
        self.onRetry = onRetry
    }

    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "newspaper.fill")
                .font(.system(size: 56))
                .foregroundColor(.secondary)

            VStack(spacing: 8) {
                Text(title)
                    .font(.title3.bold())

                Text(message)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
            }

            if let onRetry = onRetry {
                Button(action: onRetry) {
                    Label("Retry", systemImage: "arrow.clockwise")
                        .font(.headline)
                        .foregroundColor(.white)
                        .padding(.horizontal, 28)
                        .padding(.vertical, 12)
                        .background(Color.accentColor)
                        .cornerRadius(10)
                }
            }
        }
        .padding(32)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

// MARK: - Article Error View
struct ArticleErrorView: View {
    let errorMessage: String
    let onRetry: (() -> Void)?

    var body: some View {
        VStack(spacing: 20) {
            Image(systemName: "exclamationmark.triangle.fill")
                .font(.system(size: 56))
                .foregroundColor(.orange)

            VStack(spacing: 8) {
                Text("Something went wrong")
                    .font(.title3.bold())

                Text(errorMessage)
                    .font(.subheadline)
                    .foregroundColor(.secondary)
                    .multilineTextAlignment(.center)
            }

            if let onRetry = onRetry {
                Button(action: onRetry) {
                    Label("Try Again", systemImage: "arrow.clockwise")
                        .font(.headline)
                        .foregroundColor(.white)
                        .padding(.horizontal, 28)
                        .padding(.vertical, 12)
                        .background(Color.accentColor)
                        .cornerRadius(10)
                }
            }
        }
        .padding(32)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

// MARK: - Reading Time Helper
private func estimatedReadingTime(for content: String) -> Int {
    let wordsPerMinute = 200
    let words = content.components(separatedBy: .whitespacesAndNewlines).count
    let minutes = Int(ceil(Double(words) / Double(wordsPerMinute)))
    return max(1, minutes)
}

// MARK: - Previews
struct ArticlesScreen_Previews: PreviewProvider {
    static var previews: some View {
        ArticlesScreen()
    }
}
