import SwiftUI

class DivContext: ObservableObject {
    @Published var variables: [String: String] = [:]
    let actionHandler: DivActionHandler

    init(actionHandler: DivActionHandler = DivCompositeActionHandler(handlers: [])) {
        self.actionHandler = actionHandler
    }

    func binding(for key: String) -> Binding<String> {
        return Binding(
            get: { self.variables[key] ?? "" },
            set: { self.variables[key] = $0 }
        )
    }
}
