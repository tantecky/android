#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;
varying vec2 v_top;
varying vec2 v_bot;
varying vec2 v_left;
varying vec2 v_right;

uniform sampler2D u_pressure;
uniform sampler2D u_velocity;

uniform float u_dt;

void main(){
    // r = ux, g = uy, b = wx, a = wy
    // u = velocity
    // w = divergence non-free velocity

    float ux = texture2D(u_velocity, v_center).r;
    float uy = texture2D(u_velocity, v_center).g;

    float wx = texture2D(u_velocity, v_center).b;
    float wy = texture2D(u_velocity, v_center).a;

    // r = pressure, g = gradx pressure, b = gray pressure
    float dpx = texture2D(u_pressure, v_center).g;
    float dpy = texture2D(u_pressure, v_center).b;

//    gl_FragColor = vec4(wx - dpx, wy - dpy, 0.0, 0.0);
    float uxNew = ux + u_dt * (wx - dpx);
    float uyNew = uy + u_dt * (wy - dpy);

    gl_FragColor = vec4(uxNew, uyNew, 0.0, 0.0);
}
