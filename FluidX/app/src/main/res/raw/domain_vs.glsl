attribute vec4 a_position;

varying vec2 v_texCoord;

uniform mat4 u_mvp;

void main() {
    v_texCoord = vec2(a_position);
    gl_Position = u_mvp * a_position;
}