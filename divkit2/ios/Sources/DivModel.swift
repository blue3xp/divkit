import SwiftUI

// --- Base Models ---

enum DivOrientation {
    case vertical, horizontal, overlap
}

enum DivAlignment {
    case start, center, end, top, bottom
}

struct DivAction {
    var url: URL? = nil
    var logId: String? = nil
}

struct DivBorder {
    var color: Color = .black
    var width: CGFloat = 0
    var cornerRadius: CGFloat = 0
}

struct DivStyle {
    var width: CGFloat? = nil
    var height: CGFloat? = nil
    var background: Color? = nil
    var padding: CGFloat = 0
    var margin: CGFloat = 0
    var border: DivBorder? = nil
    var action: DivAction? = nil
}

// --- Component Hierarchy ---

enum DivComponent: Identifiable {
    var id: UUID { UUID() }

    case text(content: String, fontSize: CGFloat, color: Color, fontWeight: Font.Weight, style: DivStyle)
    case image(url: URL?, contentScale: ContentMode, style: DivStyle)
    case button(text: String, action: DivAction, backgroundColor: Color, textColor: Color, style: DivStyle)
    case container(items: [DivComponent], orientation: DivOrientation, alignment: Alignment, style: DivStyle)
}

// Helper for Color
extension Color {
    init(hex: UInt) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xff) / 255,
            green: Double((hex >> 08) & 0xff) / 255,
            blue: Double((hex >> 00) & 0xff) / 255,
            opacity: 1
        )
    }
}
