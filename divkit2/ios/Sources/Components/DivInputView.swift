import SwiftUI

struct DivInputView: View {
    let hint: String
    let variable: String
    let style: DivStyle

    @EnvironmentObject var context: DivContext

    var body: some View {
        TextField(hint, text: context.binding(for: variable))
            .textFieldStyle(RoundedBorderTextFieldStyle())
            .applyDivStyle(style)
    }
}
