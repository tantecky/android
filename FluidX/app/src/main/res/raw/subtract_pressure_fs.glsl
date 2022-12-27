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
uniform sampler2D u_velocity;

float gradx(float left, float right) {
    return 0.5 * (right - left) / u_dx;
}

float grady(float top, float bot) {
    return 0.5 * (top - bot) / u_dy;
}

void main(){
    float pT = texture2D(u_pressure, v_top).r;
    float pB = texture2D(u_pressure, v_bot).r;
    float pL = texture2D(u_pressure, v_left).r;
    float pR = texture2D(u_pressure, v_right).r;

    float dpx = gradx(pL, pR);
    float dpy = grady(pT, pB);

    //    if (v_right.x > 1.0 || v_left.x < 0.0) {
    //        dpx = 0.0;
    //    }
    //
    //    if (v_top.y > 1.0 || v_bot.y < 0.0) {
    //        dpy = 0.0;
    //    }

    vec2 uC = texture2D(u_velocity, v_center).rg;

    gl_FragColor = vec4(uC - vec2(dpx, dpy), 0.0, 1.0);
}
