#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform vec2 u_resolution;
uniform float u_widthTexel;
uniform float u_heightTexel;
uniform float u_time;
uniform sampler2D u_temperature;

vec3 coldColor = vec3(0.0, 0.0, 1.0);
vec3 hotColor = vec3(1.0, 0.0, 0.0);

void main(){
    float temperature = texture2D(u_temperature, v_texCoord).x;
    vec3 color = mix(coldColor, hotColor, temperature);
    gl_FragColor = vec4(color, 1.0);
}
