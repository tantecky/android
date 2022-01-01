#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_center;
varying vec2 v_top;
varying vec2 v_bot;
varying vec2 v_left;
varying vec2 v_right;

uniform float u_dx;
uniform float u_dy;
uniform sampler2D u_velocity;

void main(){
    // r = ux, g = uy, b = wx, a = wy
    // u = velocity
    // w = non-divergent free velocity

    float uxC = texture2D(u_temperature, v_center).r;
    float uxT = texture2D(u_temperature, v_top).r;
    float uxB = texture2D(u_temperature, v_bot).r;
    float uxL = texture2D(u_temperature, v_left).r;
    float uxR = texture2D(u_temperature, v_right).r;

    float xNew = (xC + u_dx * (xR + xL) + u_dy * (xT + xB)) / (1.0 + 2.0 * u_dx + 2.0 * u_dy);

    gl_FragColor = vec4(xNew, 0.0, 0.0, 0.0);
}
