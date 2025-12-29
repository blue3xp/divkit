import SwiftUI

struct ContentView: View {
    @State private var divData: DivComponent? = nil
    @State private var isLoading = true

    var body: some View {
        Group {
            if isLoading {
                ProgressView("Loading JSON...")
            } else if let data = divData {
                ScrollView {
                    DivRenderer(component: data)
                }
            } else {
                Text("Failed to load data")
            }
        }
        .task {
            await loadJson()
        }
    }

    private func loadJson() async {
        // Simulate complex JSON string
        let complexJson = """
        {
          "type": "container",
          "style": { "padding": 16 },
          "orientation": "vertical",
          "items": [
            {
              "type": "text",
              "text": "Parsed from JSON (Async Task)",
              "font_size": 24,
              "font_weight": "bold",
              "style": { "margin": 8 }
            },
            {
              "type": "image",
              "url": "https://example.com/logo.png",
              "style": {
                "width": 100,
                "height": 100,
                "background": "#FFEEEEEE"
              }
            },
            {
              "type": "container",
              "orientation": "horizontal",
              "style": { "margin": 16 },
              "items": [
                {
                  "type": "button",
                  "text": "Cancel",
                  "background_color": "#FFE0E0E0",
                  "text_color": "#FF000000",
                  "style": { "margin": 4 }
                },
                {
                  "type": "button",
                  "text": "Confirm",
                  "background_color": "#FF2196F3",
                  "text_color": "#FFFFFFFF",
                  "style": { "margin": 4 }
                }
              ]
            }
          ]
        }
        """

        // Parsing on background thread implicitly via Task
        // For strict guarantees, we can use Task.detached
        let parsed: DivComponent? = await Task.detached {
            guard let data = complexJson.data(using: .utf8) else { return nil }
            do {
                // This is where Codable magic happens
                return try JSONDecoder().decode(DivComponent.self, from: data)
            } catch {
                print("JSON Parsing Error: \(error)")
                return nil
            }
        }.value

        // Artificial delay
        try? await Task.sleep(nanoseconds: 1_000_000_000)

        self.divData = parsed
        self.isLoading = false
    }
}
