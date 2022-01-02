#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;
varying vec2 v_top;
varying vec2 v_bot;
varying vec2 v_left;
varying vec2 v_right;

uniform float u_dx;
uniform float u_dy;

uniform sampler2D u_pressure;

float gradx(float left, float right) {
    return 0.5 * (right - left) / u_dx;
}

float grady(float top, float bot) {
    return 0.5 * (top - bot) / u_dy;
}

void main(){
    // r = pressure, g = gradx pressure, b = gray pressure
    float p = texture2D(u_pressure, v_center).r;
    float pT = texture2D(u_pressure, v_top).r;
    float pB = texture2D(u_pressure, v_bot).r;
    float pL = texture2D(u_pressure, v_left).r;
    float pR = texture2D(u_pressure, v_right).r;

    gl_FragColor = vec4(p, gradx(pL, pR), grady(pT, pB), 0.0);
}
