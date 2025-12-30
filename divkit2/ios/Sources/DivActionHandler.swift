import Foundation

protocol DivActionHandler {
    func handleAction(_ action: DivAction, context: DivContext) -> Bool
}

class DivFormActionHandler: DivActionHandler {
    func handleAction(_ action: DivAction, context: DivContext) -> Bool {
        if action.logId == "submit_form" {
            let formData = context.variables
            print("DivFormActionHandler: Submitting Form Data to \(String(describing: action.url)): \(formData)")
            // In a real app, perform network request here
            return true
        }
        return false
    }
}

class DivCompositeActionHandler: DivActionHandler {
    let handlers: [DivActionHandler]

    init(handlers: [DivActionHandler]) {
        self.handlers = handlers
    }

    func handleAction(_ action: DivAction, context: DivContext) -> Bool {
        for handler in handlers {
            if handler.handleAction(action, context: context) {
                return true
            }
        }
        return false
    }
}
