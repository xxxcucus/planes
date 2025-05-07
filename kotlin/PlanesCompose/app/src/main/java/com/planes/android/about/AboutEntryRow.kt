package com.planes.android.about

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutEntryRow(movie: AboutEntryModel) {



    Column(modifier = Modifier.padding(4.dp).
    fillMaxWidth())
    {
        /*Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Surface(
                modifier = Modifier.padding(12.dp).size(100.dp),
                shape = RectangleShape,
                shadowElevation = 5.dp
            ) {
                //Icon(imageVector = Icons.Default.Favorite, contentDescription = "Movie Image")
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(
                                data = movie.images[0]
                            ).apply(block = fun ImageRequest.Builder.() {
                                crossfade(true)
                                transformations(CircleCropTransformation())
                            }).build()
                    ),
                    contentDescription = "Movie Poster"
                )
            }
            Column(modifier = Modifier.padding(4.dp)) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(text = "Director: ${movie.director}")
                Text(text = "Released: ${movie.year}")

                AnimatedVisibility(visible = expanded) {
                    Column() {
                        Text(text = "Plot: ${movie.plot}")
                        HorizontalDivider()
                        Text(text = "Actors: ${movie.actors}")
                        Text(text = "Rating: ${movie.rating}")
                    }
                }

                Icon(
                    imageVector = if (!expanded) Icons.Filled.KeyboardArrowDown else Icons.Filled.KeyboardArrowUp,
                    contentDescription = "Arrow Down",
                    modifier = Modifier.size(25.dp)
                        .clickable {
                            expanded = !expanded
                        },
                    tint = Color.DarkGray
                )
            }
        }*/
    }
}