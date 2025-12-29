import SwiftUI

class DivContext: ObservableObject {
    @Published var variables: [String: String] = [:]

    func binding(for key: String) -> Binding<String> {
        return Binding(
            get: { self.variables[key] ?? "" },
            set: { self.variables[key] = $0 }
        )
    }
}
