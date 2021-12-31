#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord;

uniform float u_widthTexel;
uniform float u_heightTexel;
uniform float u_dx;
uniform float u_dy;
uniform float u_sink;
uniform sampler2D u_temperature;

void main(){
    vec2 top = vec2(v_texCoord.x, v_texCoord.y + u_heightTexel);
    vec2 bot = vec2(v_texCoord.x, v_texCoord.y - u_heightTexel);
    vec2 left = vec2(v_texCoord.x - u_widthTexel, v_texCoord.y);
    vec2 right = vec2(v_texCoord.x + u_widthTexel, v_texCoord.y);

    float xT = texture2D(u_temperature, top).x;
    float xB = texture2D(u_temperature, bot).x;
    float xL = texture2D(u_temperature, left).x;
    float xR = texture2D(u_temperature, right).x;

    float xC = texture2D(u_temperature, v_texCoord).x;
    // explicit formulation
    // float xNew = xC + u_dx * (xR - 2.0 * xC + xL) + u_dy * (xT - 2.0 * xC + xB);
    // implicit jacobi method
    float xNew = (xC + u_dx * (xR + xL) + u_dy * (xT + xB)) / (1.0 + 2.0 * u_dx + 2.0 * u_dy);
    xNew -= u_sink;
    xNew = max(xNew, 0.0);

    gl_FragColor = vec4(xNew, 0.0, 0.0, 1.0);
}
