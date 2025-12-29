import SwiftUI

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
