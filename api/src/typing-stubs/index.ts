import session from "express-session"

declare module "express-session" {
    // eslint-disable-next-line
    export interface SessionData {
        username: string
        admin: boolean
    }
}

export type OdesliResponse = {
    // The unique ID for the input entity that was supplied in the request. The
    // data for this entity, such as title, artistName, etc. will be found in
    // an object at `nodesByUniqueId[entityUniqueId]`
    entityUniqueId: string,
  
    // The userCountry query param that was supplied in the request. It signals
    // the country/availability we use to query the streaming platforms. Defaults
    // to 'US' if no userCountry supplied in the request.
    //
    // NOTE: As a fallback, our service may respond with matches that were found
    // in a locale other than the userCountry supplied
    userCountry: string,
  
    // A URL that will render the Songlink page for this entity
    pageUrl: string,
  
    // A collection of objects. Each key is a platform, and each value is an
    // object that contains data for linking to the match
    linksByPlatform: {
      // Each key in `linksByPlatform` is a Platform. A Platform will exist here
      // only if there is a match found. E.g. if there is no YouTube match found,
      // then neither `youtube` or `youtubeMusic` properties will exist here
      [K in Platform]: {
        // The unique ID for this entity. Use it to look up data about this entity
        // at `entitiesByUniqueId[entityUniqueId]`
        entityUniqueId: string,
  
        // The URL for this match
        url: string,
  
        // The native app URI that can be used on mobile devices to open this
        // entity directly in the native app
        nativeAppUriMobile?: string,
  
        // The native app URI that can be used on desktop devices to open this
        // entity directly in the native app
        nativeAppUriDesktop?: string,
      }
    }
  };
  
  type Platform =
    | 'spotify'
    | 'itunes'
    | 'appleMusic'
    | 'youtube'
    | 'youtubeMusic'
    | 'google'
    | 'googleStore'
    | 'pandora'
    | 'deezer'
    | 'tidal'
    | 'amazonStore'
    | 'amazonMusic'
    | 'soundcloud'
    | 'napster'
    | 'yandex'
    | 'spinrilla'
    | 'audius'
    | 'audiomack';
  
  type APIProvider =
    | 'spotify'
    | 'itunes'
    | 'youtube'
    | 'google'
    | 'pandora'
    | 'deezer'
    | 'tidal'
    | 'amazon'
    | 'soundcloud'
    | 'napster'
    | 'yandex'
    | 'spinrilla'
    | 'audius'
    | 'audiomack';
