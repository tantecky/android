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
uniform float u_viscosity;

uniform sampler2D u_velocity;
uniform sampler2D u_force;

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

    float ux = texture2D(u_velocity, v_center).r;
    float uxT = texture2D(u_velocity, v_top).r;
    float uxB = texture2D(u_velocity, v_bot).r;
    float uxL = texture2D(u_velocity, v_left).r;
    float uxR = texture2D(u_velocity, v_right).r;

    float uy = texture2D(u_velocity, v_center).g;
    float uyT = texture2D(u_velocity, v_top).g;
    float uyB = texture2D(u_velocity, v_bot).g;
    float uyL = texture2D(u_velocity, v_left).g;
    float uyR = texture2D(u_velocity, v_right).g;

    float fx = texture2D(u_force, v_center).r;
    float fy = texture2D(u_force, v_center).g;

    float wx = u_viscosity * lap(ux, uxL, uxR, uxT, uxB) + fx;
    float wy = u_viscosity * lap(uy, uyL, uyR, uyT, uyB) + fy;

    wx += -(ux * gradx(uxL, uxR) + uy * grady(uxT, uxB));
    wy += -(ux * gradx(uyL, uyR) + uy * grady(uyT, uyB));

    gl_FragColor = vec4(ux, uy, wx, wy);
}
