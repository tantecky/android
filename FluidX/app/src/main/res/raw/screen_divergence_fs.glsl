#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;

uniform sampler2D u_divergence;

void main(){
    float div = texture2D(u_divergence, v_center).r;
    gl_FragColor = vec4(div, 0.0, 0.0, 1.0);
}
