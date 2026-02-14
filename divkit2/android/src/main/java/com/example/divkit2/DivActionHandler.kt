package com.example.divkit2

interface DivActionHandler {
    fun handleAction(action: DivAction, context: DivContext): Boolean
}

class DivFormActionHandler : DivActionHandler {
    override fun handleAction(action: DivAction, context: DivContext): Boolean {
        if (action.logId == "submit_form") {
            val formData = context.getAllVariables()
            println("DivFormActionHandler: Submitting Form Data to ${action.url}: $formData")
            // In a real app, perform network request here
            return true
        }
        return false
    }
}

class DivCompositeActionHandler(private val handlers: List<DivActionHandler>) : DivActionHandler {
    override fun handleAction(action: DivAction, context: DivContext): Boolean {
        for (handler in handlers) {
            if (handler.handleAction(action, context)) {
                return true
            }
        }
        return false
    }
}
