#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_center;
varying vec2 v_top;
varying vec2 v_bot;
varying vec2 v_left;
varying vec2 v_right;

uniform sampler2D u_temperature;

void main(){
    float xC = texture2D(u_temperature, v_center).x;
    xC -= 0.001;
    xC = max(xC, 0.0);

    gl_FragColor = vec4(xC, 0.0, 0.0, 0.0);
}
