//
// Created by neo on 9/20/26.
//

import Foundation
import SwiftUI
import SharedLogic

struct SystemInfo: Identifiable {
    let id = UUID()
    let label: String
    let value: String
}

struct AboutScreen: View {
    let platform = Platform()

    var systemDataItems: [SystemInfo] {
        [
            SystemInfo(label: "Operating System", value: "\(platform.osName), \(platform.osVersion)"),
            SystemInfo(label: "Device", value: platform.deviceModel),
            SystemInfo(label: "Density", value: "\(platform.density)")
        ]
    }

    var body: some View {
        NavigationStack {
            ScrollView{
                LazyVStack(spacing: 8){
                    ForEach(Array(systemDataItems.enumerated()),id: \.element.id){ index, item in
                        VStack(alignment: .leading, spacing: 4){
                            Text(item.label)
                                .font(.caption)
                                .foregroundColor(.gray)
                            Text(item.value)
                                .font(.body)
                        }
                        .frame(
                            maxWidth: .infinity,
                            alignment: .leading
                        )
                        .padding(.vertical,4)
                       
                        if index < systemDataItems.count - 1 {
                            Divider()
                        }
                    }
                }
                .padding()
                .background(
                    RoundedRectangle(cornerRadius: 16)
                    .fill(Color.gray.opacity(0.15)))
                .padding(.horizontal, 16)
            }.navigationTitle("About Screen")
        }
    }
}

struct AboutScreen_Previews: PreviewProvider {
    static var previews: some View {
        AboutScreen()
    }
}
