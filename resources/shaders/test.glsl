#type vertex
#version 130

attribute highp vec2 aPos;
attribute mediump vec3 aColor;

uniform mat4 uProjection;
uniform mat4 uView;

out vec2 fPos;
out vec3 fColor;


void main() {
    fColor = aColor;
    fPos = aPos;
    gl_Position = uProjection * uView * vec4(aPos, 0.0, 1.0);
}

#type fragment
#version 130

#define PI radians(180)

in vec2 fPos;
in vec3 fColor;

uniform float uTime;
uniform float uSpace;
uniform vec2 uCenter;
uniform float uInverted;
uniform vec2 uVec0Pos;
uniform vec3 uVec0Color;
uniform vec2 uVec1Pos;
uniform vec3 uVec1Color;
uniform vec2 uVec2Pos;
uniform vec3 uVec2Color;

out vec4 color;

float foo(float t, float offset) {
    t += offset;
    float step = mod(t, 3.0f);
    if (1.0f < step && step < 2.0f) {
        return 0.0f;
    } else if (mod(t - 1, 6) > 4) {
        return (cos(t * PI) + 1) / 2;
    }
    return (-cos(t * PI) + 1) / 2;
}

float getRad(vec2 pos, vec2 center) {
    float dx = pos[0] - center[0];
    float dy = pos[1] - center[1];
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

float getVal(float rad, float offset) {
    return 2 * (sin(rad + offset) + 1) / 3;
}

void main() {
    float rad = getRad(fPos, uCenter);
    float vec0Rad = getRad(uVec0Pos, uCenter);
    float vec1Rad = getRad(uVec1Pos, uCenter);
    float vec2Rad = getRad(uVec2Pos, uCenter);
    if (rad >= vec0Rad && rad <= vec1Rad) {// is in between vec0 and vec1
        float dRadVec01 = vec1Rad - vec0Rad;
        float dRadVec0Rat = (rad - vec0Rad) / dRadVec01;
        float dRadVec1Rat = (vec1Rad - rad) / dRadVec01;
        color = vec4(
                dRadVec0Rat * uVec1Color[0] + dRadVec1Rat * uVec0Color[0],
                dRadVec0Rat * uVec1Color[1] + dRadVec1Rat * uVec0Color[1],
                dRadVec0Rat * uVec1Color[2] + dRadVec1Rat * uVec0Color[2],
                1.0f);
    } else if (rad >= vec1Rad && rad <= vec2Rad) {// is in between vec1 and vec2
        float dRadVec12 = vec2Rad - vec1Rad;
        float dRadVec1Rat = (rad - vec1Rad) / dRadVec12;
        float dRadVec2Rat = (vec2Rad - rad) / dRadVec12;
        color = vec4(
        dRadVec1Rat * uVec2Color[0] + dRadVec2Rat * uVec1Color[0],
        dRadVec1Rat * uVec2Color[1] + dRadVec2Rat * uVec1Color[1],
        dRadVec1Rat * uVec2Color[2] + dRadVec2Rat * uVec1Color[2],
        1.0f);
    } else {// is in between vec2 and vec0
        float dRadVec20 = vec0Rad + (2 * PI - vec2Rad);
        float dRadVec2Rat;
        if (rad <= PI) {
            dRadVec2Rat = rad + (2 * PI - vec2Rad);
        } else {
            dRadVec2Rat = rad - vec2Rad;
        }
        float dRadVec0Rat;
        if (rad <= PI) {
            dRadVec0Rat = vec0Rad - rad;
        } else {
            dRadVec0Rat = vec0Rad + (2 * PI - rad);
        }
        color = vec4(
        dRadVec2Rat * uVec0Color[0] + dRadVec0Rat * uVec2Color[0],
        dRadVec2Rat * uVec0Color[1] + dRadVec0Rat * uVec2Color[1],
        dRadVec2Rat * uVec0Color[2] + dRadVec0Rat * uVec2Color[2],
        1.0f);
    }
}