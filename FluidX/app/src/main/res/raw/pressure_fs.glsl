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
uniform float u_dx2;
uniform float u_dy2;

uniform sampler2D u_pressure;
uniform sampler2D u_velocity;

float gradx(float left, float right) {
    return 0.5 * (right - left) / u_dx;
}

float grady(float top, float bot) {
    return 0.5 * (top - bot) / u_dy;
}

float div(float left, float right, float top, float bot) {
    return 0.5 * ((right - left) / u_dx + (top - bot) / u_dy);
}

float lap(float center, float left, float right, float top, float bot) {
    return (right - 2.0 * center + left) / u_dx2 + (top - 2.0 * center + bot) / u_dy2;
}

void main(){
    // r = ux, g = uy, b = wx, a = wy
    // u = velocity
    // w = divergence non-free velocity

//    float wx = texture2D(u_velocity, v_center).b;
    float wxL = texture2D(u_velocity, v_left).b;
    float wxR = texture2D(u_velocity, v_right).b;

//    float wy = texture2D(u_velocity, v_center).a;
    float wyT = texture2D(u_velocity, v_top).a;
    float wyB = texture2D(u_velocity, v_bot).a;

    // r = pressure, g = gradx pressure, b = gray pressure
    float p = texture2D(u_pressure, v_center).r;
    float pT = texture2D(u_pressure, v_top).r;
    float pB = texture2D(u_pressure, v_bot).r;
    float pL = texture2D(u_pressure, v_left).r;
    float pR = texture2D(u_pressure, v_right).r;

    float pNew = (div(wxL, wxR, wyT, wyB) + lap(0.0, pL, pR, pT, pB)) / (1.0 + 2.0 / u_dx2 +  2.0 / u_dy2);

    gl_FragColor = vec4(pNew, 0.0, 0.0, 0.0);
}
