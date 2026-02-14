import SwiftUI

struct DivCustomView: View {
    let message: String
    let plugin: String
    let style: DivStyle

    var body: some View {
        // Mock API Call simulation
        // In reality, this would look up a view factory or plugin manager
        Group {
            if plugin == "homehub" && message == "getTutorialCard" {
                VStack(alignment: .leading, spacing: 8) {
                    Text("Tutorial Card")
                        .font(.headline)
                    Text("Learn how to use DivKit2 features in this interactive tutorial.")
                        .font(.caption)
                    Button("Start Tutorial") {
                        print("Tutorial started")
                    }
                    .padding(.top, 4)
                }
                .padding()
                .background(Color.blue.opacity(0.1))
                .cornerRadius(12)
            } else {
                Text("Custom: \(plugin) / \(message)")
                    .padding()
                    .background(Color.gray.opacity(0.2))
                    .cornerRadius(8)
            }
        }
        .applyDivStyle(style)
    }
}
