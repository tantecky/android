#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;

uniform sampler2D u_force;

void main(){
    float fx = texture2D(u_force, v_center).r;
    float fy = texture2D(u_force, v_center).g;

    gl_FragColor = vec4(fx, fy, 0.0, 0.0);
}
