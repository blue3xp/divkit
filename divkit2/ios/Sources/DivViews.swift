import SwiftUI

struct DivTextView: View {
    let content: String
    let fontSize: CGFloat
    let color: Color
    let fontWeight: Font.Weight
    let style: DivStyle

    var body: some View {
        Text(content)
            .font(.system(size: fontSize, weight: fontWeight))
            .foregroundColor(color)
            .applyDivStyle(style)
    }
}

struct DivImageView: View {
    let url: URL?
    let contentScale: ContentMode
    let style: DivStyle

    var body: some View {
        // Placeholder implementation
        // Real implementation would use AsyncImage
        Rectangle()
            .fill(Color.gray.opacity(0.3))
            .overlay(
                Text("Image")
                    .foregroundColor(.gray)
            )
            .applyDivStyle(style)
    }
}

struct DivButtonView: View {
    let text: String
    let action: DivAction
    let backgroundColor: Color
    let textColor: Color
    let style: DivStyle

    var body: some View {
        Button(action: {
            print("Button Action: \(action)")
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

struct DivContainerView: View {
    let items: [DivComponent]
    let orientation: DivOrientation
    let alignment: Alignment
    let style: DivStyle

    var body: some View {
        // We need to apply style to the container itself
        // But logic depends on orientation

        switch orientation {
        case .vertical:
            VStack(alignment: alignment.horizontal) {
                ForEach(items) { item in
                    DivRenderer(component: item)
                }
            }
            .applyDivStyle(style)

        case .horizontal:
            HStack(alignment: alignment.vertical) {
                ForEach(items) { item in
                    DivRenderer(component: item)
                }
            }
            .applyDivStyle(style)

        case .overlap:
            ZStack(alignment: alignment) {
                ForEach(items) { item in
                    DivRenderer(component: item)
                }
            }
            .applyDivStyle(style)
        }
    }
}
