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

uniform sampler2D u_velocity;

float div(float left, float right, float top, float bot) {
    return 0.5 * ((right - left) / u_dx + (top - bot) / u_dy);
}

void main(){
    vec4 uC = texture2D(u_velocity, v_center);

    float uxL = texture2D(u_velocity, v_left).r;
    float uxR = texture2D(u_velocity, v_right).r;

    float uyT = texture2D(u_velocity, v_top).g;
    float uyB = texture2D(u_velocity, v_bot).g;

    // no slip BC, may be try bounce
    if (v_right.x > 1.0) {
        uxR = 0.0;
//        uxR = -uC.r;
    }

    if (v_left.x < 0.0) {
        uxL = 0.0;
//        uxL = -uC.r;
    }

    if (v_top.y > 1.0) {
        uyT = 0.0;
//        uyT = -uC.g;
    }

    if (v_bot.y < 0.0) {
        uyB = 0.0;
//        uyB = -uC.g;
    }

    gl_FragColor = vec4(div(uxL, uxR, uyT, uyB), 0.0, 0.0, 1.0);
}
