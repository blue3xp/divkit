import SwiftUI

struct DivInputView: View {
    let hint: String
    let variable: String
    let validators: [DivValidator]
    let style: DivStyle

    @EnvironmentObject var context: DivContext
    @State private var errorMessage: String? = nil

    var body: some View {
        VStack(alignment: .leading, spacing: 4) {
            TextField(hint, text: context.binding(for: variable))
                .textFieldStyle(RoundedBorderTextFieldStyle())
                .onChange(of: context.variables[variable] ?? "") { newValue in
                    validate(input: newValue)
                }

            if let error = errorMessage {
                Text(error)
                    .font(.caption)
                    .foregroundColor(.red)
                    .padding(.leading, 4)
            }
        }
        .applyDivStyle(style)
    }

    private func validate(input: String) {
        var msg: String? = nil
        for validator in validators {
            do {
                let regex = try NSRegularExpression(pattern: validator.regex)
                let range = NSRange(location: 0, length: input.utf16.count)
                if regex.firstMatch(in: input, options: [], range: range) == nil {
                    msg = validator.message
                    break
                }
            } catch {
                print("Invalid Regex: \(validator.regex)")
            }
        }
        self.errorMessage = msg
    }
}
