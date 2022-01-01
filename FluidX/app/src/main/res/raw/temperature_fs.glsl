#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_center;
varying vec2 v_top;
varying vec2 v_bot;
varying vec2 v_left;
varying vec2 v_right;

uniform float u_fx;
uniform float u_fy;
uniform float u_sink;
uniform sampler2D u_temperature;

void main(){
    float xC = texture2D(u_temperature, v_center).x;
    float xT = texture2D(u_temperature, v_top).x;
    float xB = texture2D(u_temperature, v_bot).x;
    float xL = texture2D(u_temperature, v_left).x;
    float xR = texture2D(u_temperature, v_right).x;
    // explicit formulation
    // float xNew = xC + u_fx * (xR - 2.0 * xC + xL) + u_fy * (xT - 2.0 * xC + xB);
    // implicit jacobi method
    float xNew = (xC + u_fx * (xR + xL) + u_fy * (xT + xB)) / (1.0 + 2.0 * u_fx + 2.0 * u_fy);
    xNew -= u_sink;
    xNew = max(xNew, 0.0);

    gl_FragColor = vec4(xNew, 0.0, 0.0, 0.0);
}
