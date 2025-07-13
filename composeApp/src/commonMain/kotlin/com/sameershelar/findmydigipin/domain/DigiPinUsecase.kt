@file:Suppress("LocalVariableName")

package com.sameershelar.findmydigipin.domain

object DigiPinUsecase {

    /**
     * Generates a Digipin based on latitude and longitude.
     * The Digipin is a 10-character string that represents a specific location.
     * The first character is always a letter, and the rest can be letters or numbers.
     * The format is: LLLL-LLL-LLL
     *
     * @param lat Latitude of the location (between 2.5 and 38.5)
     * @param lon Longitude of the location (between 63.5 and 99.5)
     * @return A Digipin string or an empty string if the coordinates are out of bounds.
     */
    fun getDigiPin(lat: Double, lon: Double): String {
        // Grid labels
        val L = arrayOf(
            arrayOf("F", "C", "9", "8"),
            arrayOf("J", "3", "2", "7"),
            arrayOf("K", "4", "5", "6"),
            arrayOf("L", "M", "P", "T")
        )
        var minLat = 2.5
        var maxLat = 38.5
        var minLon = 63.5
        var maxLon = 99.5
        val latDivBy = 4
        val lonDivBy = 4

        if (lat !in minLat..maxLat || lon !in minLon..maxLon) {
            return ""
        }

        val result = StringBuilder()
        var row: Int
        var col: Int

        repeat(10) { level ->
            val latStep = (maxLat - minLat) / latDivBy
            val lonStep = (maxLon - minLon) / lonDivBy

            // find row
            var top = maxLat
            var bottom = maxLat - latStep
            row = 0
            for (i in 0 until latDivBy) {
                if (lat in bottom..top) {
                    row = i
                    break
                }
                top = bottom
                bottom = top - latStep
            }

            // find column
            var left = minLon
            var right = minLon + lonStep
            col = 0
            for (i in 0 until lonDivBy) {
                if (lon in left..right) {
                    col = i
                    break
                }
                if (left + lonStep < maxLon) {
                    left = right
                    right = left + lonStep
                } else {
                    col = i
                }
            }

            // level 1 out-of-bound check
            if (level == 0 && L[row][col] == "0") {
                return ""
            }

            result.append(L[row][col])
            if (level == 2 || level == 5) {
                result.append('-')
            }

            // update bounding box
            minLat = bottom
            maxLat = top
            minLon = left
            maxLon = right
        }

        return result.toString()
    }

    /**
     * Converts a Digipin to its corresponding latitude and longitude.
     * The Digipin must be a 10-character string in the format LLLL-LLL-LLL.
     *
     * @param digiPin The Digipin string to convert.
     * @return A pair of latitude and longitude, or (-1.0, -1.0) if the Digipin is invalid.
     */
    fun getLatLngByDigipin(digiPin: String): Pair<Double, Double> {
        val pin = digiPin.replace("-", "")
        if (pin.length != 10) return Pair(-1.0, -1.0)

        val L = arrayOf(
            charArrayOf('F', 'C', '9', '8'),
            charArrayOf('J', '3', '2', '7'),
            charArrayOf('K', '4', '5', '6'),
            charArrayOf('L', 'M', 'P', 'T')
        )

        var minLat = 2.50
        var maxLat = 38.50
        var minLng = 63.50
        var maxLng = 99.50

        val latDivBy = 4
        val lngDivBy = 4

        var lat1 = 0.0
        var lat2 = 0.0
        var lng1 = 0.0
        var lng2 = 0.0

        for (lvl in 0 until 10) {
            val ch = pin[lvl]
            val latDivVal = (maxLat - minLat) / latDivBy
            val lngDivVal = (maxLng - minLng) / lngDivBy

            var ri = -1
            var ci = -1

            loop@ for (r in 0 until latDivBy) {
                for (c in 0 until lngDivBy) {
                    if (L[r][c] == ch) {
                        ri = r
                        ci = c
                        break@loop
                    }
                }
            }
            if (ri == -1 || ci == -1) Pair(-1.0, -1.0)

            lat1 = maxLat - latDivVal * (ri + 1)
            lat2 = maxLat - latDivVal * ri
            lng1 = minLng + lngDivVal * ci
            lng2 = minLng + lngDivVal * (ci + 1)

            minLat = lat1
            maxLat = lat2
            minLng = lng1
            maxLng = lng2
        }

        val cLat = (lat1 + lat2) / 2
        val cLng = (lng1 + lng2) / 2


        return Pair(cLat, cLng)
    }
}
