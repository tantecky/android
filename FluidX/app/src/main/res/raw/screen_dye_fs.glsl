#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;

uniform sampler2D u_dye;

void main(){
    float fx = texture2D(u_dye, v_center).r;
    float fy = texture2D(u_dye, v_center).g;

    gl_FragColor = vec4(fx + fy, 0.0, 0.0, 1.0);
}
