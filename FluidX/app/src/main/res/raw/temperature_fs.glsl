#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform vec2 u_resolution;
uniform float u_widthTexel;
uniform float u_heightTexel;
uniform float u_time;
uniform sampler2D u_temperature;

void main(){
    float temperature = texture2D(u_temperature, v_texCoord).x;
    gl_FragColor = vec4(v_texCoord.x, 0.0, 0.0, 1.0);
}
