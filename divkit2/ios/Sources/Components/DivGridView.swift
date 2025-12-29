import SwiftUI

struct DivGridView: View {
    let items: [DivComponent]
    let columnCount: Int
    let style: DivStyle

    var body: some View {
        let columns = Array(repeating: GridItem(.flexible(), spacing: 0), count: columnCount)

        LazyVGrid(columns: columns, spacing: 0) {
            ForEach(items) { item in
                DivRenderer(component: item)
            }
        }
        .applyDivStyle(style)
    }
}
