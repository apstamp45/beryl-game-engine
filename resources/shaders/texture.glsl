#type vertex
#version 130

attribute highp vec2 aPos;
attribute mediump vec3 aColor;
attribute highp vec2 aTexCoords;

uniform mat4 uProjection;
uniform mat4 uView;

out vec3 fColor;
out vec2 fTexCoords;

void main() {
    fColor = aColor;
    fTexCoords = aTexCoords;
    gl_Position = uProjection * uView * vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 130

uniform sampler2D texSampler;

in vec3 fColor;
in vec2 fTexCoords;

out vec4 color;

void main() {
    color = texture(texSampler, fTexCoords);
}
