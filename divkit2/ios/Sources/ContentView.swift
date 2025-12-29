import SwiftUI

struct ContentView: View {
    @State private var divData: DivComponent? = nil
    @State private var isLoading = true
    @StateObject private var divContext = DivContext()

    var body: some View {
        Group {
            if isLoading {
                ProgressView("Loading JSON...")
            } else if let data = divData {
                ScrollView {
                    DivRenderer(component: data)
                }
                .environmentObject(divContext) // Inject context
            } else {
                Text("Failed to load data")
            }
        }
        .task {
            await loadJson()
        }
    }

    private func loadJson() async {
        // Simulate complex JSON string with FORM data
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
              "type": "input",
              "hint": "Enter your name (iOS)",
              "variable": "user_name",
              "style": { "margin": 8, "width": -1 }
            },
            {
              "type": "container",
              "orientation": "horizontal",
              "style": { "margin": 16 },
              "items": [
                {
                  "type": "button",
                  "text": "Submit Form",
                  "action": { "log_id": "submit_form", "url": "https://example.com/api/submit" },
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
