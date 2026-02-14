import SwiftUI

struct ContentView: View {
    @State private var divData: DivComponent? = nil
    @State private var isLoading = true

    // Initialize handlers
    // Note: In a real app, this might be injected
    @StateObject private var divContext = DivContext(
        actionHandler: DivCompositeActionHandler(handlers: [DivFormActionHandler()])
    )

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
        // Simulate complex JSON string with FORM data and VALIDATION
        let complexJson = """
        {
            "templates": {
                "tutorialCard": {
                    "message":"getTutorialCard",
                    "plugin":"homehub"
                }
            },
            "card": {
              "type": "container",
              "style": { "padding": 16 },
              "orientation": "vertical",
              "items": [
                {
                  "type": "text",
                  "text": "Parsed from JSON (Templates)",
                  "font_size": 24,
                  "font_weight": "bold",
                  "style": { "margin": 8 }
                },
                "tutorialCard",
                {
                  "type": "text",
                  "text": "Flex Wrap Layout Example:",
                  "font_size": 18,
                  "font_weight": "bold",
                  "style": { "margin": 8, "padding": 0 }
                },
                {
                  "type": "container",
                  "orientation": "wrap",
                  "style": {
                      "background": "#FFEEEEEE",
                      "padding": 8,
                      "margin": 8,
                      "border": { "color": "#FFCCCCCC", "width": 1, "radius": 8 }
                  },
                  "items": [
                    { "type": "text", "text": "Tag 1", "style": { "background": "#FFBBDEFB", "padding": 8, "margin": 4, "border": { "radius": 16 } } },
                    { "type": "text", "text": "Long Tag Number 2", "style": { "background": "#FFC8E6C9", "padding": 8, "margin": 4, "border": { "radius": 16 } } },
                    { "type": "text", "text": "Tag 3", "style": { "background": "#FFFFECB3", "padding": 8, "margin": 4, "border": { "radius": 16 } } },
                    { "type": "text", "text": "Another Tag 4", "style": { "background": "#FFE1BEE7", "padding": 8, "margin": 4, "border": { "radius": 16 } } },
                    { "type": "text", "text": "Tag 5", "style": { "background": "#FFFFCCBC", "padding": 8, "margin": 4, "border": { "radius": 16 } } },
                    { "type": "text", "text": "Tag 6", "style": { "background": "#FFCFD8DC", "padding": 8, "margin": 4, "border": { "radius": 16 } } },
                    { "type": "text", "text": "Very Very Long Tag 7", "style": { "background": "#FFF0F4C3", "padding": 8, "margin": 4, "border": { "radius": 16 } } }
                  ]
                },
                {
                  "type": "input",
                  "hint": "Enter your name (Required)",
                  "variable": "user_name",
                  "validators": [
                     { "regex": "^.+$", "message": "Name cannot be empty" }
                  ],
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
        }
        """

        // Parsing on background thread implicitly via Task
        // For strict guarantees, we can use Task.detached
        let parsed: DivComponent? = await Task.detached {
            guard let data = complexJson.data(using: .utf8) else { return nil }
            do {
                // This is where Codable magic happens
                let root = try JSONDecoder().decode(DivRoot.self, from: data)
                return root.resolve()
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
