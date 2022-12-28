#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;

uniform sampler2D u_dye;

void main(){
    float fx = texture2D(u_dye, v_center).r;
    gl_FragColor = vec4(fx, 0.0, 0.0, 1.0);
}
