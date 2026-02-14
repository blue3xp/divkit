import SwiftUI

struct FlowLayout: Layout {
    var spacing: CGFloat

    init(spacing: CGFloat = 8) {
        self.spacing = spacing
    }

    func sizeThatFits(proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) -> CGSize {
        let width = proposal.width ?? .infinity
        var height: CGFloat = 0
        var currentX: CGFloat = 0
        var currentY: CGFloat = 0
        var currentRowHeight: CGFloat = 0

        for view in subviews {
            let size = view.sizeThatFits(ProposedViewSize(width: nil, height: nil))

            if currentX + size.width > width {
                // New row
                currentX = 0
                currentY += currentRowHeight + spacing
                currentRowHeight = 0
            }

            currentRowHeight = max(currentRowHeight, size.height)
            currentX += size.width + spacing
        }

        height = currentY + currentRowHeight
        return CGSize(width: width, height: height)
    }

    func placeSubviews(in bounds: CGRect, proposal: ProposedViewSize, subviews: Subviews, cache: inout ()) {
        let width = bounds.width
        var currentX: CGFloat = bounds.minX
        var currentY: CGFloat = bounds.minY
        var currentRowHeight: CGFloat = 0

        for view in subviews {
            let size = view.sizeThatFits(ProposedViewSize(width: nil, height: nil))

            if currentX + size.width > bounds.maxX {
                // New row
                currentX = bounds.minX
                currentY += currentRowHeight + spacing
                currentRowHeight = 0
            }

            view.place(at: CGPoint(x: currentX, y: currentY), proposal: ProposedViewSize(size))

            currentRowHeight = max(currentRowHeight, size.height)
            currentX += size.width + spacing
        }
    }
}
