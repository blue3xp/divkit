import SwiftUI

struct DivContainerView: View {
    let items: [DivComponent]
    let orientation: DivOrientation
    let alignment: Alignment
    let style: DivStyle

    var body: some View {
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
                    LazyVStack(alignment: alignment.horizontal) {
                        ForEach(items) { item in
                            DivRenderer(component: item)
                        }
                    }
                }

            case .scrollHorizontal:
                ScrollView(.horizontal) {
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
