#ifdef GL_ES
precision mediump float;
precision mediump sampler2D;
#endif

varying vec2 v_texCoord;

uniform vec2 u_resolution;
uniform float u_widthTexel;
uniform float u_heightTexel;
uniform float u_time;
uniform sampler2D u_temperature;

void main(){
    float temperature = fract(texture2D(u_temperature, v_texCoord).x + 0.005);

    if (v_texCoord.x > 0.5) {
        temperature = 0.0;
    }

    gl_FragColor = vec4(temperature, 0.0, 0.0, 1.0);
}
