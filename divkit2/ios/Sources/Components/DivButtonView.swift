import SwiftUI

struct DivButtonView: View {
    let text: String
    let action: DivAction
    let backgroundColor: Color
    let textColor: Color
    let style: DivStyle

    @EnvironmentObject var context: DivContext

    var body: some View {
        Button(action: {
            print("Button Action: \(action)")

            if action.logId == "submit_form" {
                let formData = context.variables
                print("Submitting Form Data to \(String(describing: action.url)): \(formData)")
                // In a real app, network request happens here
            }
        }) {
            Text(text)
                .foregroundColor(textColor)
                .padding()
                .background(backgroundColor)
                .cornerRadius(8) // Default styling for button
        }
        .applyDivStyle(style)
    }
}
