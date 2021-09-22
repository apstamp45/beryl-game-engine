#type vertex
#version 130

attribute highp vec2 aPos;
attribute mediump vec4 aColor;

uniform mat4 uProjection;
uniform mat4 uView;

out vec4 fColor;

void main() {
    fColor = aColor;
    gl_Position = uProjection * uView * vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 130

in vec4 fColor;

out vec4 color;

void main() {
    color = fColor;
}
