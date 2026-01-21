import SwiftUI

// --- Base Models ---

enum DivOrientation: String, Codable {
    case vertical, horizontal, overlap, wrap
    case scrollVertical = "scroll_vertical"
    case scrollHorizontal = "scroll_horizontal"
}

enum DivAlignment: String, Codable {
    case start, center, end, top, bottom
}

struct DivAction: Codable {
    var url: URL? = nil
    var logId: String? = nil

    enum CodingKeys: String, CodingKey {
        case url
        case logId = "log_id"
    }
}

struct DivBorder: Codable {
    var color: Color = .black
    var width: CGFloat = 0
    var cornerRadius: CGFloat = 0

    enum CodingKeys: String, CodingKey {
        case color, width
        case cornerRadius = "radius"
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let colorHex = try container.decodeIfPresent(String.self, forKey: .color)
        if let hex = colorHex {
            self.color = Color(hex: hex)
        }
        width = try container.decodeIfPresent(CGFloat.self, forKey: .width) ?? 0
        cornerRadius = try container.decodeIfPresent(CGFloat.self, forKey: .cornerRadius) ?? 0
    }

    init(color: Color, width: CGFloat, cornerRadius: CGFloat) {
        self.color = color
        self.width = width
        self.cornerRadius = cornerRadius
    }
}

struct DivValidator: Codable {
    var regex: String
    var message: String
}

struct DivStyle: Codable {
    var width: CGFloat? = nil
    var height: CGFloat? = nil
    var background: Color? = nil
    var padding: CGFloat = 0
    var margin: CGFloat = 0
    var border: DivBorder? = nil
    var action: DivAction? = nil
    var columnSpan: Int? = nil
    var rowSpan: Int? = nil

    enum CodingKeys: String, CodingKey {
        case width, height, background, padding, margin, border, action
        case columnSpan = "column_span"
        case rowSpan = "row_span"
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        width = try container.decodeIfPresent(CGFloat.self, forKey: .width)
        height = try container.decodeIfPresent(CGFloat.self, forKey: .height)
        padding = try container.decodeIfPresent(CGFloat.self, forKey: .padding) ?? 0
        margin = try container.decodeIfPresent(CGFloat.self, forKey: .margin) ?? 0
        border = try container.decodeIfPresent(DivBorder.self, forKey: .border)
        action = try container.decodeIfPresent(DivAction.self, forKey: .action)
        columnSpan = try container.decodeIfPresent(Int.self, forKey: .columnSpan)
        rowSpan = try container.decodeIfPresent(Int.self, forKey: .rowSpan)

        if let bgHex = try container.decodeIfPresent(String.self, forKey: .background) {
            self.background = Color(hex: bgHex)
        }
    }

    // Default init for manual creation if needed
    init(width: CGFloat? = nil, height: CGFloat? = nil, background: Color? = nil, padding: CGFloat = 0, margin: CGFloat = 0, border: DivBorder? = nil, action: DivAction? = nil, columnSpan: Int? = nil, rowSpan: Int? = nil) {
        self.width = width
        self.height = height
        self.background = background
        self.padding = padding
        self.margin = margin
        self.border = border
        self.action = action
        self.columnSpan = columnSpan
        self.rowSpan = rowSpan
    }
}

// --- Component Hierarchy ---

enum DivComponent: Identifiable, Codable {
    // Computed property to access the stable ID stored in the cases
    var id: UUID {
        switch self {
        case .text(let id, _, _, _, _, _): return id
        case .input(let id, _, _, _, _): return id
        case .image(let id, _, _, _): return id
        case .button(let id, _, _, _, _, _): return id
        case .container(let id, _, _, _, _): return id
        case .grid(let id, _, _, _): return id
        }
    }

    case text(id: UUID = UUID(), content: String, fontSize: CGFloat, color: Color, fontWeight: Font.Weight, style: DivStyle)
    case input(id: UUID = UUID(), hint: String, variable: String, validators: [DivValidator] = [], style: DivStyle)
    case image(id: UUID = UUID(), url: URL?, contentScale: ContentMode, style: DivStyle)
    case button(id: UUID = UUID(), text: String, action: DivAction, backgroundColor: Color, textColor: Color, style: DivStyle)
    case container(id: UUID = UUID(), items: [DivComponent], orientation: DivOrientation, alignment: Alignment, style: DivStyle)
    case grid(id: UUID = UUID(), items: [DivComponent], columnCount: Int, style: DivStyle)

    enum CodingKeys: String, CodingKey {
        case type, style
        // Text
        case text, fontSize = "font_size", textColor = "text_color", fontWeight = "font_weight"
        // Input
        case hint, variable, validators
        // Image
        case url, scale
        // Button
        case action, backgroundColor = "background_color"
        // Container
        case items, orientation, alignmentHorizontal = "alignment_horizontal", alignmentVertical = "alignment_vertical"
        // Grid
        case columnCount = "column_count"
    }

    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let type = try container.decode(String.self, forKey: .type)
        let style = try container.decodeIfPresent(DivStyle.self, forKey: .style) ?? DivStyle()
        let id = UUID() // Generate stable ID once upon decoding

        switch type {
        case "text":
            let content = try container.decode(String.self, forKey: .text)
            let size = try container.decodeIfPresent(CGFloat.self, forKey: .fontSize) ?? 16
            let colorHex = try container.decodeIfPresent(String.self, forKey: .textColor)
            let weightStr = try container.decodeIfPresent(String.self, forKey: .fontWeight)

            let color = colorHex != nil ? Color(hex: colorHex!) : .black
            let weight: Font.Weight = weightStr == "bold" ? .bold : .regular

            self = .text(id: id, content: content, fontSize: size, color: color, fontWeight: weight, style: style)

        case "input":
            let hint = try container.decodeIfPresent(String.self, forKey: .hint) ?? ""
            let variable = try container.decode(String.self, forKey: .variable)
            let validators = try container.decodeIfPresent([DivValidator].self, forKey: .validators) ?? []
            self = .input(id: id, hint: hint, variable: variable, validators: validators, style: style)

        case "image":
            let urlStr = try container.decodeIfPresent(String.self, forKey: .url)
            let scaleStr = try container.decodeIfPresent(String.self, forKey: .scale)

            let url = urlStr != nil ? URL(string: urlStr!) : nil
            let scale: ContentMode = scaleStr == "fill" ? .fill : .fit

            self = .image(id: id, url: url, contentScale: scale, style: style)

        case "button":
            let text = try container.decodeIfPresent(String.self, forKey: .text) ?? "Button"
            let action = try container.decodeIfPresent(DivAction.self, forKey: .action) ?? DivAction()
            let bgHex = try container.decodeIfPresent(String.self, forKey: .backgroundColor)
            let txtHex = try container.decodeIfPresent(String.self, forKey: .textColor)

            let bg = bgHex != nil ? Color(hex: bgHex!) : .blue
            let txt = txtHex != nil ? Color(hex: txtHex!) : .white

            self = .button(id: id, text: text, action: action, backgroundColor: bg, textColor: txt, style: style)

        case "container":
            let items = try container.decode([DivComponent].self, forKey: .items)
            let orientation = try container.decodeIfPresent(DivOrientation.self, forKey: .orientation) ?? .vertical

            // Map separate alignment keys to SwiftUI Alignment
            let hAlign = try container.decodeIfPresent(DivAlignment.self, forKey: .alignmentHorizontal) ?? .start
            let vAlign = try container.decodeIfPresent(DivAlignment.self, forKey: .alignmentVertical) ?? .top

            let alignment: Alignment = Self.mapAlignment(h: hAlign, v: vAlign)

            self = .container(id: id, items: items, orientation: orientation, alignment: alignment, style: style)

        case "grid":
            let items = try container.decode([DivComponent].self, forKey: .items)
            let columnCount = try container.decodeIfPresent(Int.self, forKey: .columnCount) ?? 2
            self = .grid(id: id, items: items, columnCount: columnCount, style: style)

        default:
            // Fallback for unknown types
            self = .text(id: id, content: "Unknown type: \(type)", fontSize: 14, color: .red, fontWeight: .regular, style: style)
        }
    }

    func encode(to encoder: Encoder) throws {
        // Encoding logic not required for this task
    }

    static func mapAlignment(h: DivAlignment, v: DivAlignment) -> Alignment {
        switch (h, v) {
        case (.center, .center): return .center
        case (.start, .top): return .topLeading
        case (.center, .top): return .top
        case (.end, .top): return .topTrailing
        case (.start, .bottom): return .bottomLeading
        case (.center, .bottom): return .bottom
        case (.end, .bottom): return .bottomTrailing
        case (.start, .center): return .leading
        case (.end, .center): return .trailing
        default: return .topLeading
        }
    }
}

// Helper for Color
extension Color {
    init(hex: String) {
        let hex = hex.trimmingCharacters(in: CharacterSet.alphanumerics.inverted)
        var int: UInt64 = 0
        Scanner(string: hex).scanHexInt64(&int)
        let a, r, g, b: UInt64
        switch hex.count {
        case 3: // RGB (12-bit)
            (a, r, g, b) = (255, (int >> 8) * 17, (int >> 4 & 0xF) * 17, (int & 0xF) * 17)
        case 6: // RGB (24-bit)
            (a, r, g, b) = (255, int >> 16, int >> 8 & 0xFF, int >> 8 & 0xFF)
        case 8: // ARGB (32-bit)
            (a, r, g, b) = (int >> 24, int >> 16 & 0xFF, int >> 8 & 0xFF, int & 0xFF)
        default:
            (a, r, g, b) = (1, 1, 1, 0)
        }

        self.init(
            .sRGB,
            red: Double(r) / 255,
            green: Double(g) / 255,
            blue: Double(b) / 255,
            opacity: Double(a) / 255
        )
    }
}
