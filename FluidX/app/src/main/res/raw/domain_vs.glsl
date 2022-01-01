attribute vec4 a_position;

varying vec2 v_center;
varying vec2 v_top;
varying vec2 v_bot;
varying vec2 v_left;
varying vec2 v_right;

uniform mat4 u_mvp;
uniform float u_widthTexel;
uniform float u_heightTexel;

void main() {
    v_center = vec2(a_position);
    v_top = vec2(a_position.x, a_position.y + u_heightTexel);
    v_bot = vec2(a_position.x, a_position.y - u_heightTexel);
    v_left = vec2(a_position.x - u_widthTexel, a_position.y);
    v_right = vec2(a_position.x + u_widthTexel, a_position.y);

    gl_Position = u_mvp * a_position;
}