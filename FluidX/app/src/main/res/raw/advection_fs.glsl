#ifdef GL_ES
precision highp float;
#endif

varying vec2 v_center;
varying vec2 v_top;
varying vec2 v_bot;
varying vec2 v_left;
varying vec2 v_right;

uniform sampler2D u_velocity;
uniform sampler2D u_quantity;

uniform float u_dt;
uniform float u_dx;
uniform float u_dy;
uniform float u_decay;

void main() {
    vec2 spacing = vec2(u_dx, u_dy);
    vec2 xy = v_center - u_dt * spacing * texture2D(u_velocity, v_center).rg;
    vec4 quantity = texture2D(u_quantity, xy);
    quantity /= u_decay;

    gl_FragColor = quantity;
}
