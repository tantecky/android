#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;
varying vec2 v_top;
varying vec2 v_bot;
varying vec2 v_left;
varying vec2 v_right;

uniform sampler2D u_velocity;

uniform float alpha;
uniform float beta;

void main(){
    vec4 bC = texture2D(u_velocity, v_center);
    vec4 xT = texture2D(u_velocity, v_top);
    vec4 xB = texture2D(u_velocity, v_bot);
    vec4 xL = texture2D(u_velocity, v_left);
    vec4 xR = texture2D(u_velocity, v_right);

    vec4 xNew = (xT + xB + xL + xR + alpha * bC) / beta;
    gl_FragColor = xNew;
}
