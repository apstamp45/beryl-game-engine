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

float getRad(vec2 pos, vec2 center) {
    float dx = pos.x - center.x;
    float dy = pos.y - center.y;
    if (dx == 0) {
        if (dy == 0 || dy > 0) {
            return PI / 2;
        } else {
            return 3 * PI / 2;
        }
    }
    float rad = atan(dy / dx);
    if (dx < 0) {
        rad += PI;
    }
    if (rad < 0) {
        rad += 2 * PI;
    }
    return rad;
}

vec2 rotate(vec2 pos, vec2 center, float rotate) {
    float totalRad = getRad(pos, center) + rotate;
    float distance = sqrt(pow(pos.x - center.x, 2) + pow(pos.y - center.y, 2));
    return vec2(cos(totalRad) * distance + center.x, sin(totalRad) * distance + center.y);
}

vec4 getBlur(vec2 pos, float distance, int directions, float quality, sampler2D sampler) {
    vec4 totalColor = vec4(0, 0, 0, 1);
    for (int  i = 0; i < directions; i++) {
        float direction = ((2 * PI) / directions) * i;
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
    return totalColor / (directions * (quality));
}

float getColor(float rad, float offset) {
    return 2 * (sin(rad + offset) + 1) / 3;
}

float map(float val, float min, float max, float toMin, float toMax) {
    return toMin + (val - min) * (toMax - toMin) / (max - min);
}

void main() {
    if (uIsSpacePressed != 1) {
        vec2 center = vec2(0.5f, 0.5f);
        float rot = sin(uTime) * PI;
        vec2 newPos = rotate(fTexCoords, center, rot);
//        if (newPos.x > center.x + sqrt(1.0f / 2.0f) || newPos.x < center.x - sqrt(1.0f / 2.0f) ||
//            newPos.y > center.y + sqrt(1.0f / 2.0f) || newPos.y < center.y - sqrt(1.0f / 2.0f)) {
//            color = vec4(0.0f, 0.0f, 0.0f, 0.0f);
//        } else {
            vec4 blurred = getBlur(newPos, 0.01, 16, 3, texSampler);
            color = vec4(blurred.r * getColor(rot, 0),
                         blurred.g * getColor(rot, 2 * PI / 3),
                         blurred.b * getColor(rot, 4 * PI / 3),
                         1);
//        }
    } else {
        color = texture(texSampler, fTexCoords);
    }
}
