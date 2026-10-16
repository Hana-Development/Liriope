package org.hanayori.liriope.api;

/**
 * Entry point for other plugins.
 *
 * <pre>{@code
 * IBorder border = LiriopeAPI.getApi().getBorder(world);
 * border.setRadius(100, 60);
 * }</pre>
 * <p>
 * Add {@code Liriope} as a dependency in your plugin.yml so it is enabled before you call this.
 */
public final class LiriopeAPI {

    private static ILiriopeAPI api;

    private LiriopeAPI() {
    }

    /**
     * @return the API, or {@code null} if Liriope is not enabled
     */
    public static ILiriopeAPI getApi() {
        return api;
    }

    /**
     * Internal, called by Liriope on enable/disable. Do not call this from other plugins.
     */
    public static void setApi(ILiriopeAPI api) {
        LiriopeAPI.api = api;
    }
}
