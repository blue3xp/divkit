import SwiftUI

struct ContentView: View {
    var body: some View {
        let sampleData: DivComponent = .container(
            items: [
                .text(
                    content: "Welcome to DivKit 2.0 (SwiftUI)",
                    fontSize: 24,
                    color: .black,
                    fontWeight: .bold,
                    style: DivStyle(margin: 8)
                ),
                .image(
                    url: URL(string: "https://example.com/logo.png"),
                    contentScale: .fit,
                    style: DivStyle(width: 100, height: 100, background: Color.gray.opacity(0.2))
                ),
                .container(
                    items: [
                        .button(
                            text: "Cancel",
                            action: DivAction(logId: "cancel"),
                            backgroundColor: Color.gray.opacity(0.3),
                            textColor: .black,
                            style: DivStyle(margin: 4)
                        ),
                        .button(
                            text: "Confirm",
                            action: DivAction(logId: "confirm"),
                            backgroundColor: .blue,
                            textColor: .white,
                            style: DivStyle(margin: 4)
                        )
                    ],
                    orientation: .horizontal,
                    alignment: .center,
                    style: DivStyle(margin: 16)
                )
            ],
            orientation: .vertical,
            alignment: .leading,
            style: DivStyle(padding: 16)
        )

        ScrollView {
            DivRenderer(component: sampleData)
        }
    }
}
