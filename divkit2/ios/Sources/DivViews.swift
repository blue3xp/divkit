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
        if let url = url {
            AsyncImage(url: url) { phase in
                switch phase {
                case .empty:
                    Color.gray.opacity(0.3)
                case .success(let image):
                    image.resizable()
                        .aspectRatio(contentMode: contentScale)
                case .failure:
                    Color.red.opacity(0.3) // Error state
                @unknown default:
                    Color.gray.opacity(0.3)
                }
            }
            .applyDivStyle(style)
        } else {
            Rectangle()
                .fill(Color.gray.opacity(0.3))
                .applyDivStyle(style)
        }
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

        Group {
            switch orientation {
            case .vertical:
                VStack(alignment: alignment.horizontal) {
                    ForEach(items) { item in
                        DivRenderer(component: item)
                    }
                }

            case .horizontal:
                HStack(alignment: alignment.vertical) {
                    ForEach(items) { item in
                        DivRenderer(component: item)
                    }
                }

            case .overlap:
                ZStack(alignment: alignment) {
                    ForEach(items) { item in
                        DivRenderer(component: item)
                    }
                }

            case .scrollVertical:
                ScrollView(.vertical) {
                    // Use LazyVStack for performance on large lists
                    LazyVStack(alignment: alignment.horizontal) {
                        ForEach(items) { item in
                            DivRenderer(component: item)
                        }
                    }
                }

            case .scrollHorizontal:
                ScrollView(.horizontal) {
                    // Use LazyHStack for performance on large lists
                    LazyHStack(alignment: alignment.vertical) {
                        ForEach(items) { item in
                            DivRenderer(component: item)
                        }
                    }
                }
            }
        }
        .applyDivStyle(style)
    }
}

struct DivGridView: View {
    let items: [DivComponent]
    let columnCount: Int
    let style: DivStyle

    var body: some View {
        let columns = Array(repeating: GridItem(.flexible(), spacing: 0), count: columnCount)

        LazyVGrid(columns: columns, spacing: 0) {
            ForEach(items) { item in
                DivRenderer(component: item)
                    // Note: LazyVGrid in SwiftUI doesn't support 'span' natively like Compose
                    // without breaking the flow into custom grids.
                    // For this PoC, we ignore columnSpan and render as standard flow cells.
            }
        }
        .applyDivStyle(style)
    }
}
