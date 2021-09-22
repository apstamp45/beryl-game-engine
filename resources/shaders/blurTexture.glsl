#type vertex
#version 130

attribute highp vec2 aPos;
attribute mediump vec3 aColor;
attribute highp vec2 aTexCoords;

uniform mat4 uProjection;
uniform mat4 uView;

out vec2 fPos;
out vec3 fColor;
out vec2 fTexCoords;

void main() {
    fPos = aPos;
    fColor = aColor;
    fTexCoords = aTexCoords;
    gl_Position = uProjection * uView * vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 130

#define PI radians(180)

uniform sampler2D texSampler;
uniform float uIsSpacePressed;
uniform float uTime;

in vec2 fPos;
in vec3 fColor;
in vec2 fTexCoords;

out vec4 color;

// returns a pixel that was averaged with the
// pixels in its radius <distance>.
vec4 getBlur(vec2 pos, float distance, int directions, float quality, sampler2D sampler) {
    vec4 totalColor = vec4(0, 0, 0, 1);
    // for the specified number of directions
    for (int  i = 0; i < directions; i++) {
        float direction = ((2 * PI) / directions) * i;
        // for the ammount of quality
        for (int j = 1; j <= quality; j++) {
            vec2 posToCheck = pos + vec2(cos(direction) * distance * ((1 / quality) * j),
                                         sin(direction) * distance * ((1 / quality) * j));
            if (posToCheck.x > 1 || posToCheck.x < 0 ||
                posToCheck.y > 1 || posToCheck.y < 0) {
                continue;
            }
            totalColor += texture(sampler, posToCheck);
        }
    }
    vec4 averageColor = totalColor / (directions * (quality));
    return averageColor;
}

float map(float val, float min, float max, float toMin, float toMax) {
    return toMin + (val - min) * (toMax - toMin) / (max - min);
}

void main() {
    if (uIsSpacePressed != 1) {
        color = getBlur(fTexCoords, 0.01f, 32, 10, texSampler);
        //color = getBlur(fTexCoords, map(sin(uTime), -1, 1, 0, 0.025), 16, 3, texSampler);
    } else {
        color = getBlur(fTexCoords, 0.0f, 1, 1, texSampler);
    }
}
