#ifdef GL_ES
precision mediump float;
#endif

#define DT 0.005
#define T_BC 0.0
#define NU 0.02

varying vec2 v_texCoord;

uniform vec2 u_resolution;
uniform float u_widthTexel;
uniform float u_heightTexel;
uniform float u_time;
uniform sampler2D u_temperature;

void main(){
    float xCenter = v_texCoord.x;
    float yCenter = v_texCoord.y;

    vec2 center = vec2(xCenter, yCenter);
    vec2 top = vec2(xCenter, yCenter + u_heightTexel);
    vec2 bot = vec2(xCenter, yCenter - u_heightTexel);
    vec2 left = vec2(xCenter - u_widthTexel, yCenter);
    vec2 right = vec2(xCenter + u_widthTexel, yCenter);

    float temperature = texture2D(u_temperature, center).x;
    float temperatureNew;

    // is boundary texel

    float temperatureTop = texture2D(u_temperature, top).x;
    float temperatureBot = texture2D(u_temperature, bot).x;
    float temperatureLeft = texture2D(u_temperature, left).x;
    float temperatureRight = texture2D(u_temperature, right).x;
    float dx = NU * DT / (u_widthTexel * u_widthTexel);
    float dy = NU * DT / (u_heightTexel * u_heightTexel);

    temperatureNew = temperature + dx * (temperatureRight - 2.0 * temperature + temperatureLeft) + dy * (temperatureTop - 2.0 * temperature + temperatureBot);
    temperatureNew -= 0.001f;

    temperatureNew = clamp(temperatureNew, 0.0f, 1.0f);

    gl_FragColor = vec4(temperatureNew, 0.0, 0.0, 1.0);
}
