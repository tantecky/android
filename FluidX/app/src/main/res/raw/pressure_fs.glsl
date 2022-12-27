#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;
varying vec2 v_top;
varying vec2 v_bot;
varying vec2 v_left;
varying vec2 v_right;

uniform float alpha;
uniform float beta;

uniform sampler2D u_pressure;
uniform sampler2D u_divergence;

void main(){
    float bC = texture2D(u_divergence, v_center).r;
    float xT = texture2D(u_pressure, v_top).r;
    float xB = texture2D(u_pressure, v_bot).r;
    float xL = texture2D(u_pressure, v_left).r;
    float xR = texture2D(u_pressure, v_right).r;

    float xNew = (xT + xB + xL + xR + alpha * bC) / beta;
    gl_FragColor = vec4(xNew, 0.0, 0.0, 1.0);
}
