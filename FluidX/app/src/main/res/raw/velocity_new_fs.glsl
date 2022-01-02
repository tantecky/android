#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;

uniform sampler2D u_velocity;

uniform float u_dt;

void main(){
    // r = ux, g = uy, b = wx, a = wy
    // u = velocity
    // w = divergence non-free velocity

    float ux = texture2D(u_velocity, v_center).r;
    float uy = texture2D(u_velocity, v_center).g;

    float uxNew = ux / (1.0 - u_dt);
    float uyNew = uy / (1.0 - u_dt);

    gl_FragColor = vec4(uxNew, uyNew, 0.0, 0.0);
}
