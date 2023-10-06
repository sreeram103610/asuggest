# About

An autocomplete Android application that allows users to search phrases and retrieve users from the Slack server. The application is equipped with a feature to read and write to the 'denylist' internally, ensuring it remains updated. The utilization of a trie data structure enhances the efficiency of searching for phrases to deny.
Based on MVP with modularization.

## Caching Mechanisms

The application employs two types of caches for optimized performance and enhanced user experience:

### 1. Memory Cache
- Utilizes a timed cache with space restrictions, empowered by Patricia Trie to leverage additive search.
- Efficiently handles queries like "avc", "avd" by retrieving data for all users with the prefix "av" from the cache.

### 2. Offline Cache
- Provides offline access to previously fetched data.
- Implements 'Cache-Control' in okhttp for effective caching and retrieval of network responses.

## Features

- **Offline Support:** Enables users to access previously fetched search results without an active internet connection.

## Libraries Used

- **Coil:** Employed for asynchronous image loading and caching.
- **ConstraintLayout:** Utilized for displaying User Search items and the Search Fragment efficiently.
- **Moshi:** Handles JSON serialization and deserialization for network responses.
- **OkHttp & Retrofit:** Powers network requests and responses
-
## Known Bugs

1. **RecyclerView Position Retention:**
    - The RecyclerView doesn’t retain its position upon device rotation.
    - An attempt to use 'stateRestorationPolicy' to fix the issue hasn’t been successful.

2. **Offline Mode Cache Retrieval:**
    - In offline mode, if the cache contains all the required items, they won’t be displayed until an empty ('') search is performed.
    - This issue arises due to the tying of caching to GET requests in OkHttp.

## Future Enhancements

- Address known bugs to enhance the user experience and application performance.
- Explore additional caching and data retrieval optimizations for more responsive and efficient search operations.
- Add unit tests
